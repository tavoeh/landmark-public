package com.tigerspike.landmark.data.repository

import com.google.common.truth.Truth
import com.tigerspike.landmark.util.CoroutineTestRule
import com.tigerspike.landmark.data.datasource.NoteRemoteDataSource
import com.tigerspike.landmark.domain.model.Error
import com.tigerspike.landmark.domain.model.Result
import com.tigerspike.landmark.util.mockNotes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock

/**
 * Created by Gustavo Enriquez on 25/7/20.
 **/


@ExperimentalCoroutinesApi
class NoteRepositoryImplTest {

    @get:Rule var coroutinesTestRule = CoroutineTestRule()

    private val remoteDataSource = mock(NoteRemoteDataSource::class.java)
    private val repository = NoteRepositoryImpl(
        remoteDataSource,
        coroutinesTestRule.testDispatcherProvider
    )

    @Test
    fun `GIVEN valid data WHEN get notes THEN return a valid note list`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            given(remoteDataSource.fetchNotes())
                .willReturn(Result.Success(mockNotes))

            // WHEN
            val result = repository.getNotes()

            // THEN
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)
        }
    }

    @Test
    fun `GIVEN data source error WHEN get notes THEN return failure`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            //GIVE
            val e = Exception("data source error")
            given(remoteDataSource.fetchNotes())
                .willReturn(Result.Failure(Error.DomainError(e)))

            // WHEN
            val result = repository.getNotes()

            // THEN
            Truth.assertThat(result).isInstanceOf(Result.Failure::class.java)
        }
    }



}

