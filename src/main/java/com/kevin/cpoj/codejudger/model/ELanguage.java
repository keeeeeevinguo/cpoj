package com.kevin.cpoj.codejudger.model;

import com.kevin.cpoj.domain.enumeration.ProgrammingLanguage;
import org.apache.commons.lang3.StringUtils;

public enum ELanguage
{
//  if [ "${LANGUAGE,,}" == "java" ]; then
//    executeJava
//  elif [ "${LANGUAGE,,}" == "python2" ]; then
//    executePython2
//  elif [ "${LANGUAGE,,}" == "python3" ]; then
//    executePython3
//  elif [ "${LANGUAGE,,}" == "c" ]; then
//    executeC_CPP
//  elif [ "${LANGUAGE,,}" == "c++" ]; then
//    executeC_CPP
//  else

    // Currently all languages require compilation so we can show compilation error early
    JAVA("java", ".java", true),
    C("c", ".c", true),
    CPP("c++", ".cpp", true),
    PYTHON2("python2", ".py", true),
    PYTHON3("python3", ".py", true);

    private final String languageCmd;
    private final String fileExtension;
    private final boolean requiresCompilation;

    ELanguage(final String languageCmd, final String fileExtension, final boolean requiresCompilation)
    {
        this.languageCmd = languageCmd;
        this.fileExtension = fileExtension;
        this.requiresCompilation = requiresCompilation;
    }

    public static ELanguage getLanguage(String language)
    {
        if (StringUtils.isBlank(language)) return null;
        language = language.trim().toLowerCase();
        if (language.equals("java")) return JAVA;
        else if (language.equals("c")) return C;
        else if (language.equals("cpp") || language.equals("c++")) return CPP;
        else if (language.equals("python") || language.equals("python2")) return PYTHON2;
        else if (language.equals("python3")) return PYTHON3;
        return null;
    }

    public static ELanguage getLanguage(ProgrammingLanguage language)
    {
        switch (language)
        {
            case JAVA:
                return ELanguage.JAVA;
            case C:
                return ELanguage.C;
            case C_PLUS_PLUS:
                return ELanguage.CPP;
            case PYTHON3:
                return ELanguage.PYTHON3;
            case PYTHON2:
                return ELanguage.PYTHON2;
            default:
                return null;
        }
    }

    public String getLanguageCmd()
    {
        return languageCmd;
    }

    public String getFileExtension()
    {
        return fileExtension;
    }
}
