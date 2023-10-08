This library provides access to the following Non-Deterministic random number generators

1. Quantum Random Bit Generator at Ruder Boskovic Institute (Croatia, http://random.irb.hr)
  Generation method: photonic emission in semiconductors
  (c) 2010-2011 Roberto Javier Godoy
  (c) 2007 Brendan Burns, 
  (c) 2007 Radomir Stevanovic and Rudjer Boskovic Institute.
  
2. TRNG www.random.org by Mads Haahr
  Generation method: atmospheric noise
  (c) 2008-2009 Roberto Javier Godoy
 
                                  random.org    random.irb.hr
 Peak quota                     122 KiB/day      100 MiB/day
 Maximum sustained quota      24.41 KiB/day    33.03 MiB/day
 Transport                       HTTP/HTTPS    TCP (non encrypted protocol)
 Login required                          NO              YES
 
Any suggestion is strongly encouraged and it will be enthusiastly received
by the author of this software (rjgodoy@fich.unl.edu.ar)

Caveat user: 
  I'm not good at documenting things... if you think that something is not 
  clear, or you are absolutely lost about how to using this library, just 
  write me; I will be glad to help you.

If something doesn't work as expected, please tell me (either by mail, or by 
filling a bug with the SourceForge project bug tracker 

Requirements:
 Java SE 1.6

Optional: 
 Jakarta commons-loggin library, downloadable from http://commons.apache.org/logging/

---------


Changes in v1.0
 - Add support for QRBG from Ruder Boskovic Institute 
 - Update www.random.org certificate
  
Changes in v0.2
 - Add proxy for org.apache.commons.logging.LogFactory (avoiding direct dependency on the library)
 - Fix endless loop when RjgodoyProvider is the default provider and a PRNG is used
 - Remove duplicate log messages in MH_SecureRandomSpi constructor and RjgodoyProvider
 - Fix issue when loading certificate from JAR file   
 
Changes in v0.1
 - Add trim() in MH_HttpCient.checkQuota()
 
Known Bugs
 - when using SSL, the KeyGenerator uses the default SecureRandom.
   this blocks if we are the default SecureRandom. This behaviour cannot be overriden
   using Java API. 
    