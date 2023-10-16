package mds.engine.classes

import kotlin.random.Random

data class IdentificationKey(
    val key: String,
    val id: Int = Random(Int.MAX_VALUE).nextInt()
)