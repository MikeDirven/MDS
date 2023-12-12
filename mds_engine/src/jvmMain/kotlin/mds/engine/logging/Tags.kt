package mds.engine.logging

import mds.engine.enums.RequestMethods

object Tags {
    val engine = "${Colors.cyan}Mds engine -> ${Colors.reset}"
    fun getRequest() = "${Colors.green}GET -> ${Colors.reset}"
    fun postRequest() = "${Colors.yellow}POST -> ${Colors.reset}"
    fun putRequest() = "${Colors.blue}PUT -> ${Colors.reset}"
    fun patchRequest() = "${Colors.purple}PATCH -> ${Colors.reset}"
    fun deleteRequest() = "${Colors.red}DELETE -> ${Colors.reset}"
    fun headRequest() = "${Colors.green}HEAD -> ${Colors.reset}"
    fun optionsRequest() = "${Colors.purple}OPTIONS -> ${Colors.reset}"

    fun methodColor(method: RequestMethods) : String = when(method){
        RequestMethods.GET -> getRequest()
        RequestMethods.POST -> postRequest()
        RequestMethods.PUT -> putRequest()
        RequestMethods.PATCH -> patchRequest()
        RequestMethods.DELETE -> deleteRequest()
        RequestMethods.HEAD -> headRequest()
        RequestMethods.OPTIONS -> optionsRequest()
    }
}