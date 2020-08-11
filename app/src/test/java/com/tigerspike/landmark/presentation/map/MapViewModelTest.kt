package com.tigerspike.landmark.presentation.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth.assertThat
import com.tigerspike.landmark.util.CoroutineTestRule
import com.tigerspike.landmark.domain.model.Error
import com.tigerspike.landmark.domain.model.Result
import com.tigerspike.landmark.domain.useCase.GetNotesUseCase
import com.tigerspike.landmark.presentation.ViewState
import com.tigerspike.landmark.util.mockNote
import com.tigerspike.landmark.util.mockNotes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock

/**
 * Created by Gustavo Enriquez on 26/7/20.
 **/

@ExperimentalCoroutinesApi
class MapViewModelTest {

    @get:Rule var coroutinesTestRule = CoroutineTestRule()
    @get:Rule val instantTaskRule = InstantTaskExecutorRule()

    private val getNotesUseCase = mock(GetNotesUseCase::class.java)
    private val viewModel = MapViewModel(
        getNotesUseCase,
        coroutinesTestRule.testDispatcherProvider
    )

    @Test
    fun `GIVEN valid data WHEN get notes use case executes THEN return emit valid view state`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            given(getNotesUseCase.execute())
                .willReturn(Result.Success(mockNotes to mockNotes))

            // WHEN
            viewModel.mapViewState.observeForever { }
            viewModel.getNotes()

            // THEN
            assertThat(viewModel.mapViewState.value).isNotNull()
            assertThat(viewModel.mapViewState.value).isInstanceOf(ViewState.Data::class.java)

            val notes = (viewModel.mapViewState.value as ViewState.Data).data.notes
            assertThat(notes).hasSize(mockNotes.size * 2) // User notes and other notes
            assertThat(notes.first().text).isEqualTo(mockNotes.first().text)
        }
    }

    @Test
    fun `GIVEN use case error WHEN get notes use case THEN return emit error view state`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            val e = Exception("use case error")
            given(getNotesUseCase.execute())
                .willReturn(Result.Failure(Error.DomainError(e)))

            // WHEN
            viewModel.mapViewState.observeForever { }
            viewModel.getNotes()

            // THEN
            assertThat(viewModel.mapViewState.value).isNotNull()
            assertThat(viewModel.mapViewState.value).isInstanceOf(ViewState.Failure::class.java)
        }
    }

    @Test
    fun `GIVEN valid location WHEN get select note THEN return emit valid view state`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVE
            val melbourne = LatLng(-37.8124517, 144.9595111)

            // WHEN
            viewModel.mapViewState.observeForever { }
            viewModel.setSelectedNoteLocation(melbourne)

            // THEN
            assertThat(viewModel.mapViewState.value).isNotNull()
            assertThat(viewModel.mapViewState.value).isInstanceOf(ViewState.Data::class.java)

            val mapViewState = (viewModel.mapViewState.value as ViewState.Data).data
            assertThat(mapViewState.selectedNoteLocation).isEqualTo(melbourne)
        }
    }

}