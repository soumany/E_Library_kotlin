package kh.edu.ferupp.e_library_kotlin.Modals

data class Book(
    val author: Author,
    val book_image: String,
    val book_pdf: String,
    val category: Category,
    val description: String,
    val id: Int,
    val price: String,
    val publisher: String,
    val title: String
)