# lightningbug-maven-plugin
[![GitHub issues](https://img.shields.io/github/issues/lightningbug-io/lightningbug-maven-plugin)](https://github.com/lightningbug-io/lightningbug-maven-plugin/issues)
[![Maven Central Status](https://img.shields.io/maven-central/v/io.lightningbug/lightningbug-maven-plugin)](https://mvnrepository.com/artifact/io.lightningbug)
[![Licensing](https://img.shields.io/github/license/lightningbug-io/lightningbug-maven-plugin)](https://github.com/lightningbug-io/lightningbug-maven-plugin/blob/master/LICENSE)



A Maven Plugin for Collecting Metrics for Java Projects

[Lightning Bug](http://www.lightningbug.io) is a tool that aims to collect information about software to establish a chain of trust.  

To add the lightningbug-maven-plugin to your Maven project add the following code to your pom.xml file.

    <project>
    	<build>
			<pluginManagement>
				<plugins>
					<plugin>
						<groupId>ai.lightningbug</groupId>
						<artifactId>lightningbug-maven-plugin</artifactId>
						<version>0.0.4-SNAPSHOT</version>
						<executions>
							<execution>
								<goals>
									<goal>lightningbug</goal>
								</goals>
							</execution>
						</executions>
					</plugin>
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-surefire-plugin</artifactId>
						<version>3.0.0-M4</version>
						<configuration>
							<properties>
								<property>
									<name>listener</name>
									<value>ai.lightningbug.testlisteners.JUnitRunListener</value>
								</property>
							</properties>
						</configuration>
					</plugin>
				</plugins>
			</pluginManagement>
		</build>
	</project>

Note: Please don't copy the entirety of the above code into your pom.xml file.  It won't work.  I am explicitly calling out that the plugin nodes should have a plugins parent node which has a pluginManagement parent node which has a build parent node which has a project parent node.  There are likely going to be other children nodes under project, build, etc.

## FAQ
### What is the goal of Lightning Bug?
To reduce the entropy in the quality assurance process through the establishment of a chain of trust.
### What is a chain of trust?
A chain of trust is a term from IT security used to describe the level of trust in the TLS security certificates.  As Naedele and Koch wrote in their research paper, "Trust and Tamper-Proof Software Delivery", 
> "A major design problem for the signing process is whether the person who signs the artifact, especially if he is taking a personal responsibility, has at all a realistic chance of knowing what he signs. Ideally, this would be solved by extending the tamper-proof supply chain to the individual developer taking personal, non-repudiable responsibility for every artifact he checks into the configuration management system. Other signatures could attest to successful code reviews and passing several levels of testing and other quality assurance."
Lightning Bug was built off the insight in the last sentence.

### So what is the chain of trust for Lightning Bug?
Great question!
### Why are you doing this?
During the research for a dissertation, the maintainer of this code discovered that many large technology companies have separately developed similar internal solutions for managing flakiness.  The idea behind Lightning Bug was to make a standard tool that would help other companies develop more reliable software.
### Doesn't SonarQube do this already?
Not that I can tell.  SonarQube provides static analysis of multiple programming languages but it doesn't establish a chain of trust that the software throughout the dependency tree is reliable.
### Why choose the name Lightning Bug?
The moniker Lightning Bugs (known in the UK as glow worms) was used throughout my childhood to describe the family Lampyridae of beetles that are noted for their bioluminescence. Lightning Bug seems like an appropriate term for a flaky test as well. Lightning is largely associated with stochastic events. While their bioluminescence appears random there has been amazing. research into discovering the patterns of this species. Apparently, the bioluminescence is hypothesized to be a signal from males to females. But as a child growing up in the eastern United States they are a source of wonder on many summer nights.
### What information is Lightning Bug currently collecting?
* LOC per unit, module, and project (method, class, and project in Java)
* Cyclotomic Complexity of unit and module
* Call stack for each line of code
* Hardware state at the time of build
* Hardware state at the time of test
* The test signal for each executed test
### What is the performance impact on the build and the test executions?
It will vary from project to project.  We are collecting data right now to determine the impact but the aim is to keep it as minimal as possible. 

### How are you avoiding creating Heisenbugs through the additional overhead of Lightning Bug on the SUT?

### I would like to set up my own Lightning Bug server to maintain confidentiality of my code.  How do I do that?
Currently the server side solution for Lightning Bug assumes Amazon Web Services hosting.  However, we plan on making it IaaS independent going forward.

### Are you planning on supporting other programming languages besides Java?
Yes!  If you are interested in helping in this effort you can review the API contracts and contact the development team for more details.

### Are you going to support Gradle?
Yes!  It is on the product roadmap.

### Are you going to support TestNG tests?
Yes!  It is on the product roadmap.

## Release History
### 0.0.5

* First public release

## Issues

Please consult our [issue management rules](ISSUES.md) before creating or working on issues. 
