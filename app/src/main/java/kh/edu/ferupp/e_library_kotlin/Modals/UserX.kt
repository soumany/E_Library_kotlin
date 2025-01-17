package kh.edu.ferupp.e_library_kotlin.Modals

data class UserX(
    val email: String,
    val gender: String,
    val id: Int,
    val password_hash: String,
    val role: String,
    val username: String
)