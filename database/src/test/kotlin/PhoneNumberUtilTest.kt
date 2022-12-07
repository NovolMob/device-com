import org.junit.Test
import ru.novolmob.database.utils.PhoneNumberUtil

class PhoneNumberUtilTest {
    @Test
    fun testStringNumber() {
        val number = 1234567uL
        val clientNumber = PhoneNumberUtil.stringClientNumber(number)
        assert(clientNumber == "123-456-7") { println(clientNumber) }
    }

    @Test
    fun testShortStringNumber() {
        val number = 123uL
        val clientNumber = PhoneNumberUtil.stringClientNumber(number)
        assert(clientNumber == "123") { println(clientNumber) }
    }

    @Test
    fun testLongStringNumber() {
        val number = 1234567891uL
        val clientNumber = PhoneNumberUtil.stringClientNumber(number)
        assert(clientNumber == "123-456-789-1") { println(clientNumber) }
    }

    @Test
    fun testStringPhoneNumber() {
        val string = PhoneNumberUtil.stringPhoneNumber(123uL, 123uL, 1234567uL)
        assert(string == "+123 (123) 123-456-7") { println(string) }
    }

    @Test
    fun deserializeTest() {
        val phoneNumber = PhoneNumberUtil.deserializePhoneNumber("+7 (123) 123-132-7")
        assert(phoneNumber != null) { println("phone number is null") }
        assert(phoneNumber?.countryCode == 7uL) { println(phoneNumber) }
        assert(phoneNumber?.innerCode == 123uL) { println(phoneNumber) }
        assert(phoneNumber?.clientNumber == 1231327uL) { println(phoneNumber) }
    }
}