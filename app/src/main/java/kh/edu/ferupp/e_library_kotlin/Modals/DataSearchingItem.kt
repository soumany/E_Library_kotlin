package kh.edu.ferupp.e_library_kotlin.Modals

data class DataSearchingItem(
    val author: AuthorXX,
    val book_image: String,
    val book_pdf: String,
    val category: CategoryXX,
    val description: String,
    val id: Int,
    val price: String,
    val publisher: String,
    val title: String
)