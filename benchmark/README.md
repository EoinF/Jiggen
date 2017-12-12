# Benchmark Usage

1. Install the Jiggen core subproject locally using the gradle task "installMavenLocal" 
on the root gradle project
    
    
    gradlew installMavenLocal 
    
    
2. Run 'mvn install' to generate the target folder with benchmarks.jar inside
3. Run 'java -jar benchmarks.jar' with the -h argument to see a list of commands
    
    
    To calculate throughput:
    java -jar benchmarks.jar -bm thrpt
        