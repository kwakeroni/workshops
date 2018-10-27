echo ------------------------ Exercise 4 ------------------------
echo Make workshop.my.policy.provider provide a Policy
echo ----------------------------------- ------------------------

rm -rf classes
source setPath.sh

set -e
set -x

javac --module-path mods --module-source-path "./*/src/main/java" -d classes --module workshop.persons
javac --module-path mods --module-source-path "./*/src/main/java" -d classes --module workshop.animals
javac --module-path mods --module-source-path "./*/src/main/java" -d classes --module workshop.family
javac --module-path mods --module-source-path "./*/src/main/java" -d classes --module workshop.insurance
javac --module-path mods --module-source-path "./*/src/main/java" -d classes --module workshop.my.policy.provider

jar --create --file mods/workshop.persons.jar -C classes/workshop.persons/ .
jar --create --file mods/workshop.animals.jar -C classes/workshop.animals/ .
jar --create --file mods/workshop.family.jar --main-class workshop.family.Family -C classes/workshop.family/ .
jar --create --file mods/workshop.insurance.jar --main-class workshop.insurance.FamilyInsurance -C classes/workshop.insurance/ .
jar --create --file mods/workshop.my.policy.provider.jar -C classes/workshop.my.policy.provider/ .

java -p mods -m workshop.insurance
