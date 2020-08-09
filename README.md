# juxt-tech-test

Solution to juxt technical test

## Limitations
Not a comprehensive amount of unit tests done
missing unit tests completely for hottest month and getting a forecast by city

Left all functions in core.clj, would usually split into different files

Left secrets in repo, these would usually stored in an environment variable

## Problems
No known bugs

## Installation
Follow build step in Usage section

## Usage
BUILD:
    $ lein uberjar
RUN: 
    $ java -jar ./target/uberjar/juxt-tech-test-0.1.0-SNAPSHOT-standalone.jar
REPL:
    $ lein repl
TEST:
    $ lein with-profiles dev test

## Options

No known bugs

## License

Copyright © 2020 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
