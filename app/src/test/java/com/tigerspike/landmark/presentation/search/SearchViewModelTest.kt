package com.tigerspike.landmark.presentation.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.tigerspike.landmark.util.CoroutineTestRule
import com.tigerspike.landmark.domain.model.Error
import com.tigerspike.landmark.domain.model.Result
import com.tigerspike.landmark.domain.useCase.SearchNotesByTextOrUserUseCase
import com.tigerspike.landmark.presentation.ViewState
import com.tigerspike.landmark.util.mockNotes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock

/**
 * Created by Gustavo Enriquez on 26/7/20.
 **/

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule var coroutinesTestRule = CoroutineTestRule()
    @get:Rule val instantTaskRule = InstantTaskExecutorRule()

    private val searchNotesUseCase = mock(SearchNotesByTextOrUserUseCase::class.java)
    private val viewModel = SearchViewModel(
        searchNotesUseCase,
        coroutinesTestRule.testDispatcherProvider
    )

    @Test
    fun `GIVEN valid query WHEN execute search notes use case executes THEN return emit valid view state`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            given(searchNotesUseCase.execute(ArgumentMatchers.anyString()))
                .willReturn(Result.Success(mockNotes))

            // WHEN
            viewModel.query.observeForever { }
            viewModel.notesViewState.observeForever { }
            viewModel.query.value = "query"

            // THEN
            assertThat(viewModel.notesViewState.value).isNotNull()
            assertThat(viewModel.notesViewState.value).isInstanceOf(ViewState.Data::class.java)

            val notes = (viewModel.notesViewState.value as ViewState.Data).data
            assertThat(notes).hasSize(mockNotes.size)
            assertThat(notes.first().text).isEqualTo(mockNotes.first().text)
        }
    }

    @Test
    fun `GIVEN use case error WHEN execute notes use case THEN return emit error view state`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            val e = Exception("use case error")
            given(searchNotesUseCase.execute(ArgumentMatchers.anyString()))
                .willReturn(Result.Failure(Error.DomainError(e)))

            // WHEN
            viewModel.query.observeForever { }
            viewModel.notesViewState.observeForever { }
            viewModel.query.value = "query"

            // THEN
            assertThat(viewModel.notesViewState.value).isNotNull()
            assertThat(viewModel.notesViewState.value).isInstanceOf(ViewState.Failure::class.java)
        }
    }

}