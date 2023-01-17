
import ru.novolmob.core.models.Address
import ru.novolmob.core.utils.PhoneNumberUtil
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PhoneNumberUtilTest {
    @Test
    fun testModel() {
        println(Address("dasdas").toString())
    }

    @Test
    fun testStringNumber() {
        val number = 1234567uL
        val clientNumber = PhoneNumberUtil.stringClientNumber(number)
        assertEquals(clientNumber, "123-456-7", clientNumber)
    }

    @Test
    fun testShortStringNumber() {
        val number = 123uL
        val clientNumber = PhoneNumberUtil.stringClientNumber(number)
        assertEquals(clientNumber, "123", clientNumber)
    }

    @Test
    fun testLongStringNumber() {
        val number = 1234567891uL
        val clientNumber = PhoneNumberUtil.stringClientNumber(number)
        assertEquals(clientNumber, "123-456-789-1", clientNumber)
    }

    @Test
    fun testStringPhoneNumber() {
        val string = PhoneNumberUtil.stringPhoneNumber(123uL, 123uL, 1234567uL)
        assertEquals(string, "+123 (123) 123-456-7", string)
    }

    @Test
    fun deserializeTest() {
        val phoneNumber = PhoneNumberUtil.deserializePhoneNumber("+7 (123) 123-132-7")
        assertTrue(phoneNumber != null, "phone number is null")
        assertTrue(phoneNumber.countryCode == 7uL, phoneNumber.toString())
        assertTrue(phoneNumber.innerCode == 123uL, phoneNumber.toString())
        assertTrue(phoneNumber.clientNumber == 1231327uL, phoneNumber.toString())
    }
}