# 1. Challenge approach for task length of a week

Date: 2022-07-25

## Alternate approaches if I had a week
* Would have spent more time investigating use of different threading options. For example,
ExecutorService is generally better for potentially long-running tasks such as making requests 
from URLs vs. the processor-intensive task of MD5 hashing.
* Would have done performance testing to confirm above.
* Would have added logic for handling urls to sites/files that no longer exist.
* Would have built a REST interface using a Spring controller.
* Would have built a web UI to allow the user to select an input file and thread limit as
inputs and provided realtime updates as URLs were read and hashed.
* Would have provided some sort of persistence (database) to store the results of the hashing.
* Would have designed additional test coverage.
* Would have implemented a logger to log errors and other important events.
* Would have dockerized the build for portability.