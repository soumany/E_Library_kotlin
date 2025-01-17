package kh.edu.ferupp.e_library_kotlin.Modals

data class CartDataBookItem(
    val book: BookX,
    val id: Int,
    val quantity: Int,
    val user: User
)