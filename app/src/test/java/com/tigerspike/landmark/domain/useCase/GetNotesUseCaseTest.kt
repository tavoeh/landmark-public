package com.tigerspike.landmark.domain.useCase

import com.google.common.truth.Truth.assertThat
import com.tigerspike.landmark.util.CoroutineTestRule
import com.tigerspike.landmark.domain.contract.NoteRepository
import com.tigerspike.landmark.domain.contract.UserRepository
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
 * Created by Gustavo Enriquez on 26/7/20.
 **/

@ExperimentalCoroutinesApi
class GetNotesUseCaseTest {

    @get:Rule var coroutinesTestRule = CoroutineTestRule()

    private val noteRepository = mock(NoteRepository::class.java)
    private val userRepository = mock(UserRepository::class.java)
    private val userCase = GetNotesUseCase(noteRepository, userRepository)

    @Test
    fun `GIVEN valid data WHEN get notes THEN return a valid note list`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            given(noteRepository.getNotes())
                .willReturn(Result.Success(mockNotes))

            // WHEN
            val result = userCase.execute()

            // THEN
            assertThat(result).isInstanceOf(Result.Success::class.java)
        }
    }

    @Test
    fun `GIVEN repository error WHEN get notes THEN return failure`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            //GIVE
            val e = Exception("data source error")

            given(noteRepository.getNotes())
                .willReturn(Result.Failure(Error.DomainError(e)))

            // WHEN
            val result = userCase.execute()

            // THEN
            assertThat(result).isInstanceOf(Result.Failure::class.java)
        }
    }
}