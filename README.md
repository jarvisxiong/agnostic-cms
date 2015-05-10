# Agnostic CMS

Nowadays WCMSs greatly reduce the time needed to build multi-functional websites. Unfortunately usually built-in WCMS features aren't sufficient and developers have to put an effort into extending them.

WCMSs do own the tendency to restrict options in terms of technology. They often require acquiring knowledge of the frameworks and structure they use which implies spending the time allocated to development for learning new technologies.

Agnostic CMS is a WCMS that is independent of the technologies used in building the front-end application and doesn't impact it's implementation. It also gives ability to add plugins of any web technology supporting chosen database. Front end application recieves all the information needed from database and uploaded resources on the file system without any other touch points. Plugins use the same two connection points plus Apache reverse proxy that ensures user authentication and session maintenance.
