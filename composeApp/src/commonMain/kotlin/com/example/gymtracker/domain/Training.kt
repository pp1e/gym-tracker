package com.example.gymtracker.domain

data class Training(
    val id: Long,
    val exercises: List<Exercise>,
) {
    fun filter(
        deleteExerciseIds: List<Long>,
        deleteApproachIds: List<Long>,
    ): Training =
        this.copy(
            exercises =
                this.exercises
                    .filterNot { it.id in deleteExerciseIds }
                    .map { exercise ->
                        exercise.copy(
                            approaches = exercise.approaches.filterNot { it.id in deleteApproachIds },
                        )
                    },
        )
}
