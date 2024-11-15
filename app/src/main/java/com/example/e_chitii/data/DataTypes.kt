package com.example.e_chitii.data

data class UserData (
    val userId: String? = null,
    val name: String? = null,
    val number: String? = null,
    val imageUrl: String? = null,
){
    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "number" to number,
        "imageUrl" to imageUrl)
}
data class ChatData(
    val chatId: String? = null,
    val user1: ChatUser?= ChatUser(),
    val user2: ChatUser?= ChatUser(),




    )

data class ChatUser(
    val userId: String? = null,
    val name: String? = null,
    val number: String? = null,
    val imageUrl: String? = null,

)

data class Message(
    var sendBy:String? = "",
    var message:String? = "",
    val timeStamp:String? = ""

)

data class Status(
    val user: ChatUser? = ChatUser(),
    val imageUrl: String? = "",
    val timeStamp : String? =  "",
)