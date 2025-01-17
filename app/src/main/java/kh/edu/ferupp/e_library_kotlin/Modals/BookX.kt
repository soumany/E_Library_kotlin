package kh.edu.ferupp.e_library_kotlin.Modals

data class BookX(
    val author: AuthorX,
    val book_image: String,
    val book_pdf: String,
    val category: CategoryX,
    val description: String,
    val id: Int,
    val price: String,
    val publisher: String,
    val title: String
)