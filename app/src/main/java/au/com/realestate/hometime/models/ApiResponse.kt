package au.com.realestate.hometime.models

data class ApiResponse<T>(
    val errorMessage: String? = null,
    val hasError: Boolean? = null,
    val hasResponse: Boolean? = null,
    val responseObject: List<T>? = null
)
