package de.rogallab.mobile.domain.useCases.people

import de.rogallab.android.data.models.PersonDto
import de.rogallab.mobile.domain.IPeopleRepository
import de.rogallab.mobile.domain.UiState
import de.rogallab.mobile.domain.entities.Person
import de.rogallab.mobile.domain.mapping.toDomain
import de.rogallab.mobile.domain.utilities.logDebug
import de.rogallab.mobile.domain.utilities.logError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ReadAll @Inject constructor(
   private val _peopleRepository: IPeopleRepository,
   private val _exceptionHandler: CoroutineExceptionHandler,
   private val _dispatcher: CoroutineDispatcher,
) {

   operator fun invoke(): Flow<UiState<List<Person>>> = flow {
      emit(UiState.Loading)

      _peopleRepository.selectAll().collect { peopleDto: List<PersonDto> ->
         logDebug(tag, "ReadAll.invoke() emit success")
         val people = peopleDto.toDomain()
         people.sortedBy { it.lastName }
//       val filteredPeople = people.filter {
//          it.lastName.toUpper)=.startsWith("A")
//       }
         emit(UiState.Success(data = peopleDto.toDomain()))
      }
   }.catch {
      var message = it.localizedMessage ?: it.stackTraceToString()
      logError(tag, message)
      emit(UiState.Error(message = message))
   }.flowOn(_exceptionHandler + _dispatcher)

   companion object {
                                     //12345678901234567890123
      private const val tag: String = "ok>PeopleReadAllUseCase"
   }
}