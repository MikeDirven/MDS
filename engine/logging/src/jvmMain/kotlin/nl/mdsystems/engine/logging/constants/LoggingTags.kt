package nl.mdsystems.engine.logging.constants

import java.text.DecimalFormat

/**
 * This object contains constants for logging purposes, including color codes and HTTP method tags.
 */
object LoggingTags {
    /**
     * A property that returns the name of the current thread, formatted with a trailing space.
     */
    val thread get() = "${Thread.currentThread().name} - "
    
    /**
     * An object containing color codes for use in logging.
     */
    object Colors {
        const val RED = "\u001B[31m"
        const val GREEN = "\u001B[32m"
        const val YELLOW = "\u001B[33m"
        const val BLUE = "\u001B[34m"
        const val MAGENTA = "\u001B[35m"
        const val CYAN = "\u001B[36m"
        const val RESET = "\u001B[0m"
    }
    
    /**
     * An object containing formatted HTTP method tags, using the color codes from the Colors object.
     */
    object Methods {
        const val GET = "${Colors.GREEN}GET -> ${Colors.RESET}"
        const val POST = "${Colors.YELLOW}POST -> ${Colors.RESET}"
        const val PUT = "${Colors.BLUE}PUT -> ${Colors.RESET}"
        const val PATCH = "${Colors.MAGENTA}PATCH -> ${Colors.RESET}"
        const val DELETE = "${Colors.RED}DELETE -> ${Colors.RESET}"
        const val HEAD = "${Colors.CYAN}HEAD -> ${Colors.RESET}"
    }
}