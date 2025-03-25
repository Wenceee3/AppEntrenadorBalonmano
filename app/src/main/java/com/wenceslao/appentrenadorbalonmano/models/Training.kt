package com.wenceslao.appentrenadorbalonmano.models

data class Training(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var date: String = "",
    var duration: String = "",
    var imageUrl: String = ""
) {
    // Constructor sin argumentos
    constructor() : this("", "", "", "", "", "")
}