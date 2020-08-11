package com.tigerspike.landmark.domain.useCase

import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth.assertThat
import com.tigerspike.landmark.util.CoroutineTestRule
import com.tigerspike.landmark.domain.contract.NoteRepository
import com.tigerspike.landmark.domain.contract.UserRepository
import com.tigerspike.landmark.domain.model.Error
import com.tigerspike.landmark.domain.model.Result
import com.tigerspike.landmark.domain.model.User
import com.tigerspike.landmark.util.any
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito.*
import org.mockito.Mockito.mock
import java.lang.Exception

/**
 * Created by Gustavo Enriquez on 26/7/20.
 **/

@ExperimentalCoroutinesApi
class SaveNoteUseCaseTest {

    @get:Rule var coroutinesTestRule = CoroutineTestRule()

    private val noteRepository = mock(NoteRepository::class.java)
    private val userRepository = mock(UserRepository::class.java)
    private val userCase = SaveNoteUseCase(noteRepository, userRepository)

    @Test
    fun `GIVEN valid data WHEN get save note THEN return success`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            val text = "New note"
            val melbourne = LatLng(-37.8124517, 144.9595111)
            val user = User.Authenticated("1","Name")

            given(userRepository.getUser())
                .willReturn(Result.Success(user))

            given(noteRepository.saveNote(any()))
                .willReturn(Result.Success(Unit))

            // WHEN
            val result = userCase.execute(text, melbourne.latitude, melbourne.longitude)

            // THEN
            assertThat(result).isInstanceOf(Result.Success::class.java)
        }
    }

    @Test
    fun `GIVEN user repository error WHEN get notes THEN return failure`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            val text = "New note"
            val melbourne = LatLng(-37.8124517, 144.9595111)
            val e = Exception("user repository error")

            given(userRepository.getUser())
                .willReturn(Result.Failure(Error.DomainError(e)))

            // WHEN
            val result = userCase.execute(text, melbourne.latitude, melbourne.longitude)

            // THEN
            assertThat(result).isInstanceOf(Result.Failure::class.java)
        }
    }

    @Test
    fun `GIVEN note repository error WHEN get notes THEN return failure`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            val text = "New note"
            val melbourne = LatLng(-37.8124517, 144.9595111)
            val user = User.Authenticated("1","Name")
            val e = Exception("note repository error")

            given(userRepository.getUser())
                .willReturn(Result.Success(user))

            given(noteRepository.saveNote(any()))
                .willReturn(Result.Failure(Error.DomainError(e)))

            // WHEN
            val result = userCase.execute(text, melbourne.latitude, melbourne.longitude)

            // THEN
            assertThat(result).isInstanceOf(Result.Failure::class.java)
        }
    }


}