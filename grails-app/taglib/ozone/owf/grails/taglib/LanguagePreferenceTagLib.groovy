package ozone.owf.grails.taglib

import ozone.owf.grails.OwfException

class LanguagePreferenceTagLib {
    
    static namespace = 'lang'
    def preferenceService
    
    // TODO: clean this up and test it
    // We may want to change en_US to look for js-lib/ext-2.2/source/locale/ext-lang-en.js, 
    // since en_US does not exist
    
    def preference = { attrs ->
        def retVal
        if (attrs['lang'] == null) {

            def lang = request.locale.toString()
            def result = null

            try {
              result = preferenceService.show([namespace:"owf", path:"language"])
            }
            catch (OwfException owe) {
              if ('INFO' == owe.logLevel) {
                  log.info(owe)
              }
              else if ('DEBUG' == owe.logLevel) {
                  log.debug(owe)
              }
              else {
                  log.error(owe)
              }
            }

            //check against preference
            if (result?.preference?.value != null) {
              lang = result.preference.value;
            }

            session.setAttribute("language",lang);

            retVal = """
            ${owfImport.jsOwf(path: 'lang', resource: 'ozone-lang-'+session.getAttribute("language"))}
            ${owfImport.jsLibrary(lib: 'ext', version: '4.0.7', resource : 'locale/ext-lang-'+ (session.getAttribute('language') == 'en_US' ? 'en' : session.getAttribute('language')) )}
            """
        } else {
            retVal = """
            ${owfImport.jsOwf(path: 'lang', resource: 'ozone-lang-'+attrs['lang'])}
            ${owfImport.jsLibrary(lib: 'ext', version: '4.0.7', resource : 'locale/ext-lang-'+ (attrs['lang'] == 'en_US' ? 'en' : attrs['lang']))}
            """
        }
        out << retVal
    }

}
