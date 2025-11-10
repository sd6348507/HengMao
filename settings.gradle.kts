pluginManagement {
    repositories {
        maven{ url = uri ("https://maven.aliyun.com/repository/google") } //修改
        maven{ url = uri ("https://maven.aliyun.com/repository/gradle-plugin") } //修改
        maven{ url = uri ("https://maven.aliyun.com/repository/public") } //修改
        maven{ url = uri ("https://maven.aliyun.com/repository/jcenter") } //修改
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven{ url = uri ("https://maven.aliyun.com/repository/google") } //修改
        maven{ url = uri ("https://maven.aliyun.com/repository/gradle-plugin") } //修改
        maven{ url = uri ("https://maven.aliyun.com/repository/public") } //修改
        maven{ url = uri ("https://maven.aliyun.com/repository/jcenter") } //修改
        google()
        mavenCentral()
        //配置仓库地址
        maven{ url = uri ("https://jitpack.io") }
    }
}

rootProject.name = "HengMao"
include(":app")
 