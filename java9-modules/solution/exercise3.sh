echo ------------------------ Exercise 3 ------------------------
echo Make workshop.insurance into a modular jar
echo ----------------------------------- ------------------------

rm -rf classes
source setPath.sh

set -e
set -x

javac --module-source-path "./*/src/main/java" -d classes --module workshop.persons
javac --module-source-path "./*/src/main/java" -d classes --module workshop.animals
javac --module-source-path "./*/src/main/java" -d classes --module workshop.family
javac --module-source-path "./*/src/main/java" -d classes --module workshop.insurance

jar --create --file mods/workshop.persons.jar -C classes/workshop.persons/ .
jar --create --file mods/workshop.animals.jar -C classes/workshop.animals/ .
jar --create --file mods/workshop.family.jar --main-class workshop.family.Family -C classes/workshop.family/ .
jar --create --file mods/workshop.insurance.jar --main-class workshop.insurance.FamilyInsurance -C classes/workshop.insurance/ .

java -p mods -m workshop.insurance