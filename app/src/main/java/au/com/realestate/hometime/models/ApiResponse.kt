package au.com.realestate.hometime.models

data class ApiResponse<T>(
    val responseObject: List<T>
)
