@------------------------------@
|  Celes IRC Bot               |
|  Version 2.1.0 (08/08/2016)  |
|  Created by Chad Steadman    |
@------------------------------@


INTRODUCTION
------------
This program is a simple IRC bot written in Java using the PircBot 1.5 framework
(http://www.jibble.org/pircbot.php). It can join/part channels, roll dice, remember
things, quote people, and more!


FIRST TIME SETUP
----------------
Simply double-click the "run.bat" file. This will start the bot for the first time and
generate the necessary configuration files. After you've started the "run.bat" file for
the first time, the bot will have created a new file called "bot.properties." This
file is where you will configure your bot.

NOTE: "run-silent.bat" starts the bot without the command line.


CONFIGURATION SETTINGS
----------------------
Server Address:
	The address of the IRC server you wish for your bot to connect to. This field
	cannot be left blank.

Server Port:
	The port to use when connecting to the IRC server. If left blank, it will default
	to 6667.

Server Password:
	If the server you are connecting to requires a password, put that here. If not, then
	feel free to leave it blank.

Nickname:
	The nickname you wish for your bot to use. This field cannot be left blank.

Username:
	The user name you wish for your bot to use. If left blank, it will default to
	CelesBot.

Realname:
	The real name you wish for your bot to use. If left blank, it will default to
	CelesBot vX.X.

Nickserv Password:
	If you wish for your bot to identify its nickname with NickServ, specify the
	password here. (Obviously this requires your bot's nick to be registered first,
	which you'll have to do yourself.)

Admins:
	A list of host names separated by commas (no spaces). Any user whose host name
	appears in this list will have access to the bot's administrative commands, like
	.quit, .say, and .me in addition to all the operator commands.

	e.g. "user@host.name,user2@host.name,bob@bill.com"

Operators:
	A list of host names separated by commas (no spaces). Any user whose host name
	appears in this list will have access to the bot's operator commands, like .leave
	and .forget.

Autojoin Channels:
	A list of IRC channels separated by commas (no spaces). The bot will automatically
	join any channels listed here when it first starts up.

	e.g. "#channel1,#channel2,#etc"

Banned Channels:
	A list of IRC channels to not join when sent an /INVITE. Only admins and operators can
	/INVITE the bot into banned channels.

Auto Reconnect:
	Whether or not the bot will automatically try to reconnect to the server when it gets
	disconnected. The bot will wait 20 seconds to reconnect each time if this is set to
	true.

	Valid Values: true, false, 1, 0


HOW TO USE COMMANDS
-------------------
Once your bot is connected to a server, it can receive commands via either private messages
or from within any channels in which it currently occupies. Commands are prefixed by a special
character (a period by default) followed by one of the many pre-defined command key words. You
can change these key words and the prefix in the "commands.properties" file, which was created
alongside the "bot.properties" file.

For detailed information about commands, give your bot the command ".help"


LICENSE
-------
There is none. Use this software as you see fit. The only caveat is that you cannot sell it.
Credit is appreciated for any derivative works using any of the source code bundled with this
software. :)


SPECIAL THANKS
--------------
A great deal of thanks goes to Aelanna for the Dice and DiceException classes. Thank you!
