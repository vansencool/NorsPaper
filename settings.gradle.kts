pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "NorsPaper"
for (name in listOf("NorsPaper-API", "NorsPaper-Server", "paper-api-generator")) {
    val projName = name.lowercase()
    include(projName)
    findProject(":$projName")!!.projectDir = file(name)
}
