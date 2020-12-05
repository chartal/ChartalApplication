package com.android_academy.chartal_application.repository

import com.android_academy.chartal_application.R
import com.android_academy.chartal_application.data.Actor
import com.android_academy.chartal_application.data.Movie

object DataStorage {

    fun getMoviesList(): List<Movie> {

        return listOf(
            Movie(
                "Avengers: End Game",
                "Action, Adventure, Drama",
                "125 reviews",
                "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos\\' actions and restore balance to the universe.",
                "13+",
                4.0F,
                listOf(
                    Actor(
                        R.drawable.movie,
                        "Robert Downey Jr Robert Downey Jr"
                    ),
                    Actor(
                        R.drawable.author2,
                        "Chris Evans"
                    ),
                    Actor(
                        R.drawable.author3,
                        "Mark Ruffalo"
                    ),
                    Actor(
                        R.drawable.author4,
                        "Chris Hemsworth"
                    )
                ),
                R.drawable.movie,
                R.drawable.origin,
                "137 MIN",
                false
            ),
            Movie(
                "Tenet",
                "Action, Sci-Fi, Thriller",
                "98 reviews",
                "The Protagonist is now employed by a secret organization called Tenet. A scientist shows him bullets with \"inverted\" entropy which allows them to move backwards through time. She believes they are manufactured in the future, and a weapon exists that can wipe out the past.",
                "16+",
                5.0F,
                listOf(
                    Actor(
                        R.drawable.actor2_1,
                        "John David Washington"
                    ),
                    Actor(
                        R.drawable.actor2_2,
                        "Robert Pattinson"
                    ),
                    Actor(
                        R.drawable.actor2_3,
                        "Elizabeth Debicki"
                    ),
                    Actor(
                        R.drawable.actor2_4,
                        "Dimple Kapadia"
                    ),
                    Actor(
                        R.drawable.actor2_5,
                        "Martin Donovan"
                    )
                ),
                R.drawable.movie2,
                R.drawable.tenet_big,
                "97 MIN",
                true
            ),
            Movie(
                "Black Widow",
                "Action, Adventure, Sci-Fi",
                "38 reviews",
                "Following the events of Captain America: Civil War (2016) Natasha Romanoff finds herself alone and forced to confront a dangerous conspiracy with ties to her past. Pursued by a force that will stop at nothing to bring her down, Romanoff must deal with her history as a spy and the broken relationships left in her wake long before she became an Avenger.",
                "13+",
                4.0F,
                listOf(
                    Actor(
                        R.drawable.actor3_1,
                        "Scarlett Johansson"
                    ),
                    Actor(
                        R.drawable.actor3_2,
                        "Florence Pugh"
                    ),
                    Actor(
                        R.drawable.actor3_3,
                        "David Harbour"
                    ),
                    Actor(
                        R.drawable.actor3_4,
                        "O. T. Fagbenle"
                    ),
                    Actor(
                        R.drawable.actor3_5,
                        "William Hurt"
                    )
                ),
                R.drawable.movie3,
                R.drawable.black_widow,
                "102 MIN",
                false
            ),
            Movie(
                "Wonder Woman 1984",
                "Action, Adventure, Fantasy",
                "74 reviews",
                "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos\\' actions and restore balance to the universe.",
                "13+",
                5.0F,
                listOf(
                    Actor(
                        R.drawable.actor5_1,
                        "Gal Gadot"
                    ),
                    Actor(
                        R.drawable.actor5_2,
                        "Chris Pine"
                    ),
                    Actor(
                        R.drawable.actor5_3,
                        "Kristen Wiig"
                    ),
                    Actor(
                        R.drawable.actor5_4,
                        "Pedro Pascal"
                    ),
                    Actor(
                        R.drawable.actor5_5,
                        "Robin Wright"
                    )
                ),
                R.drawable.movie4,
                R.drawable.woman_good,
                "120 MIN",
                false
            )
        )
    }
}