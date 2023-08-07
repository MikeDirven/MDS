package framework.engine.logging

import framework.engine.enums.RequestMethods

object Tags {
    val engine = "${Colors.cyan}Mds engine -> ${Colors.reset}"
    fun getRequest() = "${Colors.green}${Thread.currentThread().name} -> ${Colors.reset}"
    fun postRequest() = "${Colors.yellow}${Thread.currentThread().name} -> ${Colors.reset}"
    fun putRequest() = "${Colors.blue}${Thread.currentThread().name} -> ${Colors.reset}"
    fun patchRequest() = "${Colors.purple}${Thread.currentThread().name} -> ${Colors.reset}"
    fun deleteRequest() = "${Colors.red}${Thread.currentThread().name} -> ${Colors.reset}"
    fun headRequest() = "${Colors.green}${Thread.currentThread().name} -> ${Colors.reset}"
    fun optionsRequest() = "${Colors.purple}${Thread.currentThread().name} -> ${Colors.reset}"

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