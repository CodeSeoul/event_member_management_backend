# Contributing

Thanks for your interest in contributing! This document is a work in progress, so feel free to contribubte here, too.

Adapted from [this CONTRIBUTING.md from PurpleBook](https://gist.github.com/PurpleBooth/b24679402957c63ec426).

## Table of Contents

- [How to Help](#how-to-help)
  - [Identifying Opportunities](#identifying-opportunities)
  - [Pull Request Process](#pull-request-process)
- [Code of Conduct](#code-of-conduct)
  - [Our Pledge](#our-pledge)
  - [Our Standards](#our-standards)
  - [Our Responsibilities](#our-responsibilities)
  - [Scope](#scope)
  - [Enforcement](#enforcement)
  - [Attribution](#attribution)

When contributing to this repository, please first discuss the change you wish to make via issue,
email, or any other method with the owners of this repository before making a change.

Please note we have a code of conduct, please follow it in all your interactions with the project.

## How to Help

### Identifying Opportunities

1. Check Issues in this repository.
   - People new to the project are recommended to find something with a `good first issue` tag.
2. Discuss the issue to find any gaps in requirements or any clarification you need.
3. Request the issue be assigned to you.
4. Fork and make some changes!
5. Submit a Pull Request! See info below for details on that.

### How to Run It

1. Install [the latest JDK](https://www.oracle.com/java/technologies/downloads/) if you haven't already.
2. Install Docker for your system
   - [Windows](https://docs.docker.com/docker-for-windows/install/)
   - [Mac](https://docs.docker.com/docker-for-mac/install/)
   - [Linux](https://docs.docker.com/engine/install/#server)
3. Run `docker-compose up -d` to run the database and admin UI in the background.
   - You can access the DB UI at [http://localhost:8000/](http://localhost:8000/)
4. Run `./gradlew bootRun --args='--spring.profiles.active=local'` to build the project and run the profile with the `local profile`.
   - Note that this will automatically load some data into the database. You'll want to clean the database between runs.
5. Check the API via [http://localhost:8080/docs](http://localhost:8080/docs) using the Swagger console.

### Pull Request Process

1. Ensure any install or build dependencies are removed before the end of the layer when doing a
    build.
2. Update the README.md with details of changes to the interface, this includes new environment
variables, exposed ports, useful file locations and container parameters.
3. Increase the version numbers in any examples files and the README.md to the new version that this
Pull Request would represent. The versioning scheme we use is [SemVer](http://semver.org/).
   - **Note**: We have not yet released the project, so this step is not yet necessary.
4. You may merge the Pull Request in once you have the sign-off of two other developers, or if you
do not have permission to do that, you may request the second reviewer to merge it for you.

## Code of Conduct

### Our Pledge

In the interest of fostering an open and welcoming environment, we as
contributors and maintainers pledge to making participation in our project and
our community a harassment-free experience for everyone, regardless of age, body
size, disability, ethnicity, gender identity and expression, level of experience,
nationality, personal appearance, race, religion, or sexual identity and
orientation.

### Our Standards

Examples of behavior that contributes to creating a positive environment
include:

- Using welcoming and inclusive language
- Being respectful of differing viewpoints and experiences
- Gracefully accepting constructive criticism
- Focusing on what is best for the community
- Showing empathy towards other community members

Examples of unacceptable behavior by participants include:

- The use of sexualized language or imagery and unwelcome sexual attention or
advances
- Trolling, insulting/derogatory comments, and personal or political attacks
- Public or private harassment
- Publishing others' private information, such as a physical or electronic
address, without explicit permission
- Other conduct which could reasonably be considered inappropriate in a
professional setting

### Our Responsibilities

Project maintainers are responsible for clarifying the standards of acceptable
behavior and are expected to take appropriate and fair corrective action in
response to any instances of unacceptable behavior.

Project maintainers have the right and responsibility to remove, edit, or
reject comments, commits, code, wiki edits, issues, and other contributions
that are not aligned to this Code of Conduct, or to ban temporarily or
permanently any contributor for other behaviors that they deem inappropriate,
threatening, offensive, or harmful.

### Scope

This Code of Conduct applies both within project spaces and in public spaces
when an individual is representing the project or its community. Examples of
representing a project or community include using an official project e-mail
address, posting via an official social media account, or acting as an appointed
representative at an online or offline event. Representation of a project may be
further defined and clarified by project maintainers.

### Enforcement

Instances of abusive, harassing, or otherwise unacceptable behavior may be
reported by contacting the project team at [our Discord](https://discord.gg/Qw5DdpTVdJ). All
complaints will be reviewed and investigated and will result in a response that
is deemed necessary and appropriate to the circumstances. The project team is
obligated to maintain confidentiality with regard to the reporter of an incident.
Further details of specific enforcement policies may be posted separately.

Project maintainers who do not follow or enforce the Code of Conduct in good
faith may face temporary or permanent repercussions as determined by other
members of the project's leadership.

### Attribution

This Code of Conduct is adapted from the [Contributor Covenant][homepage], version 1.4,
available at [http://contributor-covenant.org/version/1/4][version]

[homepage]: http://contributor-covenant.org
[version]: http://contributor-covenant.org/version/1/4/
