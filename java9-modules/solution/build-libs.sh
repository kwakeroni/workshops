echo ------------------------ Classpath ------------------------
echo The application works on the classpath as before Java 9
echo ----------------------------------- -----------------------


source setPath.sh
rm -rf workshop.persons/target/classes
rm -rf workshop.animals/target/classes
rm -rf workshop.family/target/classes
rm -rf workshop.insurance/target/classes
rm -rf workshop.my.policy.provider/target/classes

set -e
set -x

mkdir -p libs

javac -d workshop.persons/target/classes workshop.persons/src/main/java/workshop/person/Person.java
jar --create --file libs/workshop.persons.jar -C workshop.persons/target/classes/ .

javac -d workshop.animals/target/classes workshop.animals/src/main/java/workshop/animal/Animal.java workshop.animals/src/main/java/workshop/animal/species/Species.java
jar --create --file libs/workshop.animals.jar -C workshop.animals/target/classes/ .

javac -cp ./libs/workshop.persons.jar:./libs/workshop.animals.jar -d workshop.family/target/classes workshop.family/src/main/java/workshop/family/Family.java
jar --create --file libs/workshop.family.jar --main-class workshop.family.Family -C workshop.family/target/classes/ .

javac -cp ./libs/workshop.persons.jar:./libs/workshop.animals.jar:./libs/workshop.family.jar -d workshop.insurance/target/classes workshop.insurance/src/main/java/workshop/insurance/FamilyInsurance.java workshop.insurance/src/main/java/workshop/insurance/Policy.java
jar --create --file libs/workshop.insurance.jar --main-class workshop.insurance.FamilyInsurance -C workshop.insurance/target/classes/ .

javac -cp ./libs/workshop.persons.jar:./libs/workshop.animals.jar:./libs/workshop.family.jar:./libs/workshop.insurance.jar:./mods/jackson-annotations-2.9.4.jar:./mods/jackson-databind-2.9.4.jar:./mods/jackson-datatype-jsr310-2.9.4.jar:./mods/jackson-core-2.9.4.jar:./mods/jackson-datatype-jdk8-2.9.4.jar -d workshop.my.policy.provider/target/classes workshop.my.policy.provider/src/main/java/workshop/my/policy/MyPolicy.java
jar --create --file libs/workshop.my.policy.provider.jar -C workshop.my.policy.provider/target/classes/ .

set +x
echo -------------------------
echo Without a Policy Provider
echo -------------------------
set -x

java -cp libs/workshop.animals.jar:libs/workshop.persons.jar:libs/workshop.family.jar:libs/workshop.insurance.jar:mods/jackson-annotations-2.9.4.jar:mods/jackson-databind-2.9.4.jar:mods/jackson-datatype-jsr310-2.9.4.jar:mods/jackson-core-2.9.4.jar:mods/jackson-datatype-jdk8-2.9.4.jar workshop.insurance.FamilyInsurance

set +x
echo -------------------------
echo With a Policy Provider
echo -------------------------
set -x

java -cp libs/workshop.animals.jar:libs/workshop.persons.jar:libs/workshop.family.jar:libs/workshop.insurance.jar:libs/workshop.my.policy.provider.jar:mods/jackson-annotations-2.9.4.jar:mods/jackson-databind-2.9.4.jar:mods/jackson-datatype-jsr310-2.9.4.jar:mods/jackson-core-2.9.4.jar:mods/jackson-datatype-jdk8-2.9.4.jar workshop.insurance.FamilyInsurance
