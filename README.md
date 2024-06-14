# RetrofitStarter
[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.kdani41/kdani-network)](https://central.sonatype.com/artifact/io.github.kdani41/kdani-network/)

### Description
Light weight library to simplify network calls using retrofit & moshi.

### Assumptions 
- Moshi used for serialization.

### Features
- Allows easy access to build retrofit instance.
- Returns the responses in sealed wrapper called [`NetworkResponse`](https://github.com/kdani41/network-helper/blob/main/library/network/src/main/java/com/kdani/core/network/NetworkResponse.kt)
- Inbuilt no network connection manager.
- Provides access to moshi, retrofit. 
- By default adds network permission for you.

### Installation 
```kotlin 

dependencies {
   implementation("io.github.kdani41:kdani-network:[version]") 
}

```

### [Usage](https://github.com/kdani41/network-helper/tree/main/app/src/main/java/com/kdani/network_helper)
#### SampleModule.kt
```kotlin 


class SampleModule {
    private const val BASE_URL = "https://sample.com/"

    @Singleton
    @Provides
    fun provideRetrofit() = RetrofitStarter.build(BASE_URL)
    
}

```

#### SampleApi.kt

```kotlin 

interface SampleApi {

    @GET("data")
    suspend fun fetchData(): NetworkResponse<NetworkModel>
    
}

```
