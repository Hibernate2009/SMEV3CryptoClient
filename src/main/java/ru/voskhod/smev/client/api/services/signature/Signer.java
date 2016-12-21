package ru.voskhod.smev.client.api.services.signature;

import org.w3c.dom.Element;
import ru.voskhod.smev.client.api.types.exception.SMEVException;
import ru.voskhod.smev.client.api.types.exception.SMEVRuntimeException;
import ru.voskhod.smev.client.api.types.exception.processing.SMEVSignatureException;

import java.io.File;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;


/**
 * Сервис электронной подписи
 */
public interface Signer {

    /**
     * Возвращает закрытый ключ по указанному псевдониму и паролю
     *
     * @param alias    псевдоним закрытого ключа
     * @param password пароль
     * @return закрытый ключ
     * @throws Exception в случае возникновения любой ошибки
     */
    PrivateKey getKey(String alias, String password) throws Exception;

    /**
     * Возвращает сертификат по указанному псевдониму
     *
     * @param alias псевдоним сертификата
     * @return сертификат
     * @throws Exception в случае возникновения любой ошибки
     */
    X509Certificate getCertificate(String alias) throws Exception;

    /**
     * Инициализирует сервис
     *
     * @param cert       псевдоним сертификата
     * @param privateKey псевдоним закрытого ключа
     * @param pass       пароль доступа закрытого ключа
     * @throws SMEVRuntimeException в случае возникновения любой ошибки
     */
    void init(String cert, String privateKey, String pass) throws SMEVRuntimeException;

    /**
     * Инициализирует сервис
     *
     * @param privateKey  закрытый ключ
     * @param certificate сертификат
     * @throws SMEVRuntimeException в случае возникновения любой ошибки
     */
    void init(PrivateKey privateKey, X509Certificate certificate) throws SMEVRuntimeException;

    /**
     * Подписывает указанный xml-элемент
     *
     * @param content2sign xml-элемент, который необходимо подписать
     * @return the element подпись в виде xml-элемента
     * @throws SMEVException в случае возникновения любой ошибки
     */
    Element sign(Element content2sign) throws SMEVException;

    /**
     * Подписывает xml-элемент, устанавливая указанный идентификатор подписи
     *
     * @param document2Sign xml-элемент, который необходимо подписать
     * @param signatureId   идентификатор цифровой подписи
     * @return подпись в виде xml-элемента
     * @throws SMEVSignatureException в случае ошибки подписи
     * @throws SMEVRuntimeException   если сервис несконфигурирован
     */
    Element signXMLDSigDetached(Element document2Sign, String signatureId) throws SMEVSignatureException, SMEVRuntimeException;

    /**
     * Подписывает указанные двоичные данные
     *
     * @param digest данные для подписи
     * @return подпись в двоичном виде
     * @throws SMEVSignatureException в случае ошибки подписи
     * @throws SMEVRuntimeException   если сервис несконфигурирован
     */
    byte[] signPKCS7Detached(byte[] digest) throws SMEVSignatureException, SMEVRuntimeException;

    /**
     * Проверяет, подписан-ли элемент подписью СМЭВ
     *
     * @param signature        подпись элемента
     * @param content2validate проверяемый элемент
     * @throws SMEVSignatureException в случае ошибки валидации, либо любых других
     */
    void validateSMEVSignature(Element signature, Element content2validate) throws SMEVSignatureException;

    /**
     * Получает дайджест указанного файла
     *
     * @param file файл для расчета
     * @return дайджест файла в двоичном виде
     * @throws SMEVSignatureException в случае возникновения любой ошибки
     */
    byte[] getDigest(File file) throws SMEVSignatureException;

    /**
     * Получает дайджест указанного битового потока
     *
     * @param inputStream битовый поток
     * @return дайджест файла в двоичном виде
     * @throws SMEVSignatureException в случае возникновения любой ошибки
     */
    byte[] getDigest(DigestInputStream inputStream) throws SMEVSignatureException;

    /**
     * Преобразует файл в битовый поток
     *
     * @param file файл для преобразования
     * @return битовый поток на основе входного файла
     * @throws SMEVSignatureException в случае возникновения любой ошибки
     */
    DigestInputStream getDigestInputStream(File file) throws SMEVSignatureException;

    /**
     * Преобразует обычный битовый поток в поток, пригодный для получения дайджеста
     *
     * @param inputStream поток ввода
     * @return битовый поток для получения дайджеста
     * @throws SMEVSignatureException в случае возникновения любой ошибки
     */
    DigestInputStream getDigestInputStream(InputStream inputStream) throws SMEVSignatureException;
}
