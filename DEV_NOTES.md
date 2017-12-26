## Set-up Notes
- Make sure to have the Discord Bot authentication key somewhere in the project, or else AvocadoBot can't login

- When testing changes to the bot, make sure to specifically cancel the daemon running the current bot before re-compiling the project. If you don't, the bot will respond with both the old behavior and the new changes.
	- _E.g., Lets say you want the bot to respond to "!a" with "Nice", instead of "Evil". If you don't cancel the old daemon before recompiling, the bot will respond with "Evil Nice"._
