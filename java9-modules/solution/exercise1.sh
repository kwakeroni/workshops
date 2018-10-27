echo ------------------------ Exercise 1 ------------------------
echo Make workshop.persons and workshop.animals into modular jars
echo ----------------------------------- ------------------------

source setPath.sh
rm -rf workshop.persons/target/classes
rm -rf workshop.animals/target/classes

set -e
set -x


javac -d workshop.persons/target/classes workshop.persons/src/main/java/workshop/person/Person.java workshop.persons/src/main/java/module-info.java
jar --create --file mods/workshop.persons.jar -C workshop.persons/target/classes/ .

javac -d workshop.animals/target/classes workshop.animals/src/main/java/workshop/animal/Animal.java workshop.animals/src/main/java/workshop/animal/species/Species.java workshop.animals/src/main/java/module-info.java
jar --create --file mods/workshop.animals.jar -C workshop.animals/target/classes/ .

jar -d --file mods/workshop.persons.jar
jar -d --file mods/workshop.animals.jar
