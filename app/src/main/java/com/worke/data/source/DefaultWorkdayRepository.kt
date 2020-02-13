package com.worke.data.source

class DefaultWorkdayRepository private constructor() {

    companion object {

        @Volatile
        private var INSTANCE: DefaultWorkdayRepository? = null

        fun getRepository(): DefaultWorkdayRepository {
            return INSTANCE ?: synchronized(this) {
                DefaultWorkdayRepository().also {
                    INSTANCE = it
                }
            }
        }
    }

    fun getWorkdays() {

    }

}
