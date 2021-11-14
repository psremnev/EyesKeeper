package com.example.eyeskeeper

import io.reactivex.subjects.BehaviorSubject

object TimerState {
    private val subject: BehaviorSubject<Constants.Timer> = BehaviorSubject.create<Constants.Timer>()
    fun getTimerSubject(): BehaviorSubject<Constants.Timer> {
        return subject
    }
}