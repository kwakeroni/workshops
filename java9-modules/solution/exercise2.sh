echo ------------------------ Exercise 2 ------------------------
echo Make workshop.family into a modular jar
echo ----------------------------------- ------------------------

rm -rf classes
source setPath.sh

set -e
set -x

javac --module-source-path "./*/src/main/java" -d classes --module workshop.persons
javac --module-source-path "./*/src/main/java" -d classes --module workshop.animals
javac --module-source-path "./*/src/main/java" -d classes --module workshop.family

jar --create --file mods/workshop.persons.jar -C classes/workshop.persons/ .
jar --create --file mods/workshop.animals.jar -C classes/workshop.animals/ .
jar --create --file mods/workshop.family.jar --main-class workshop.family.Family -C classes/workshop.family/ .

java -p mods -m workshop.family