package com.roemsoft.hengmao.exception

class ApiException : RuntimeException {

    private var code: Int? = null

    var msg: String = ""

    constructor(throwable: Throwable, code: Int) : super(throwable) {
        this.code = code
    }

    constructor(message: String) : super(Throwable(message)) {
        msg = message
    }
}