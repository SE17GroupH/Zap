# Zap
[![Build Status](https://travis-ci.org/SE17GroupH/Zap.svg?branch=master)](https://travis-ci.org/SE17GroupH/Zap)

An Android application showcasing 3 methods of authentication:
- Fingerprint
- Voice via Speaker Recognition
- via Token(or Magic) link 

### [Downloads]((https://github.com/SE17GroupH/Zap/releases))

### App Screenshots

![homepage] (/screenshots/homepage.png)
![register] (/screenshots/register.png)  
![login] (/screenshots/login.png)
![voicedna] (/screenshots/voicedna.png)  

  
### Build Project from Source:
- [Generate](https://cognitive.uservoice.com/knowledgebase/articles/864225-how-do-i-get-subscription-keys) your Subscription Keys for using Microsoft Cognitive Speaker Recognition API.
- Save the 32 characters long key as an environment variable named API_KEY_1 
  - bash: `export API_KEY_1=abcd123abc123abc123abc123abc0000`
  - In Ubuntu/Linux systems, you can declare it in `/etc/environment` or `.bashrc` file.
