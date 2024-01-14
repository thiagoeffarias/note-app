package com.compose.noteapp.di

import android.app.Application
import androidx.room.Room
import com.compose.noteapp.feature_note.data.data_source.NoteDao
import com.compose.noteapp.feature_note.data.data_source.NoteDatabase
import com.compose.noteapp.feature_note.data.repository.NoteRepositoryImpl
import com.compose.noteapp.feature_note.domain.repository.NoteRepository
import com.compose.noteapp.feature_note.domain.use_case.DeleteNote
import com.compose.noteapp.feature_note.domain.use_case.GetNotes
import com.compose.noteapp.feature_note.domain.use_case.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(context: Application): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(database: NoteDatabase): NoteRepository = NoteRepositoryImpl(database.noteDao)

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository)
        )
    }
}