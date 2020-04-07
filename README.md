# C2MON : CERN Control and Monitoring Platform
## Client extension module for fetching historical data
[![build status](https://gitlab.cern.ch/c2mon/c2mon-client-ext-history/badges/master/pipeline.svg)](https://gitlab.cern.ch/c2mon/c2mon-client-ext-history/commits/master)

The CERN Control and Monitoring Platform (C2MON) is a heterogeneous data acquisition and monitoring framework. It contains many useful features
such as historical metric persistence and browsing, command execution and alerting. It can be suitable for building many different types
of monitoring and control system.

## Documentation
See the current [reference docs][].

## Issue Tracking
Please report issues on GitLab via the [issue tracker][].

## Building from Source
C2MON uses a [Maven][]-based build system. In the instructions
below, `./mvnw` is invoked from the root of the source tree and serves as
a cross-platform, self-contained bootstrap mechanism for the build.

### Prerequisites

[Git][] and [JDK 11 or later][JDK11 build]

Be sure that your `JAVA_HOME` environment variable points to the `JDK 11` folder
extracted from the JDK download.

### Check out sources
`git clone git@github.com:c2mon/c2mon-client-ext-history.git`

### Compile and test; build all jars, distribution zips, and docs
`./mvnw build`

## Contributing
[Pull requests][] are welcome; see the [contributor guidelines][] for details.

## License
C2MON is released under the [GNU LGPLv3 License][].

[reference docs]: http://c2mon.web.cern.ch/c2mon/docs/
[issue tracker]: https://its.cern.ch/jira/issues/?jql=project%20%3D%20CM%20AND%20resolution%20%3D%20Unresolved%20AND%20component%20%3D%20%22Client%20Ext%20History%22%20ORDER%20BY%20priority%20DESC%2C%20updated%20DESC
[Maven]: http://maven.apache.org
[Git]: http://help.github.com/set-up-git-redirect
[JDK11 build]: https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
[Pull requests]: http://help.github.com/send-pull-requests
[contributor guidelines]: /CONTRIBUTING.md
[GNU LGPLv3 License]: /LICENSE
