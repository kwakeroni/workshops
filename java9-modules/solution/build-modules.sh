
set -e
set -x

rm -rf classes

source setPath.sh

javac --module-source-path "./*/src/main/java" -d classes --module workshop.persons
javac --module-source-path "./*/src/main/java" -d classes --module workshop.animals
javac --module-source-path "./*/src/main/java" -d classes --module workshop.family
javac --module-source-path "./*/src/main/java" -d classes --module workshop.insurance
javac --module-path mods --module-source-path "./*/src/main/java" -d classes --module workshop.my.policy.provider

jar --verbose --create --file mods/workshop.persons.jar -C classes/workshop.persons/ .
jar --verbose --create --file mods/workshop.animals.jar -C classes/workshop.animals/ .
jar --verbose --create --file mods/workshop.family.jar --main-class workshop.family.Family -C classes/workshop.family/ .
jar --verbose --create --file mods/workshop.insurance.jar --main-class workshop.insurance.FamilyInsurance -C classes/workshop.insurance/ .
jar --verbose --create --file mods/workshop.my.policy.provider.jar -C classes/workshop.my.policy.provider/ .

java -p mods --add-modules com.fasterxml.jackson.databind -m workshop.insurance