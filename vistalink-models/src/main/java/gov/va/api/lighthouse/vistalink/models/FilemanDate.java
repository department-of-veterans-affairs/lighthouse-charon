package gov.va.api.lighthouse.vistalink.models;

import java.io.Serial;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.Value;

@Value
public class FilemanDate {
  Instant instant;

  public FilemanDate(Instant i) {
    this.instant = i;
  }

  public static FilemanDate from(Instant i) {
    return new FilemanDate(i);
  }

  /** Static constructor for ValueOnlyXmlAttribute. */
  public static FilemanDate from(ValueOnlyXmlAttribute valueOnlyXmlAttribute, ZoneId timeZone) {
    if (valueOnlyXmlAttribute == null) {
      return null;
    }
    return from(valueOnlyXmlAttribute.value(), timeZone);
  }

  /** Static constructor for String input. */
  public static FilemanDate from(String filemanDate, ZoneId timeZone) {
    if (filemanDate == null) {
      return null;
    }
    return from(new Parser(filemanDate, timeZone).parse());
  }

  private static DecimalFormat zeroPaddedDecimal() {
    return new DecimalFormat("00");
  }

  /**
   * Returns FileManDate formatted string for a given time zone. Time information will be omitted if
   * it is midnight.Trailing zeros will be omitted after the decimal in time values. Values less
   * than 10 will be padded with a leading zero. Examples: 2970919.082701,
   * 2970919.08271,2970919.082, 2970919.08, 2970919
   *
   * <p>See http://www.vistapedia.net/index.php/Date_formats
   */
  public String formatAsDateTime(ZoneId timeZone) {
    return new DateTimeFormatter().format(instant().atZone(timeZone));
  }

  public static class BadFilemanDate extends RuntimeException {
    @Serial private static final long serialVersionUID = 2556943342836450618L;

    public BadFilemanDate(String message) {
      super(message);
    }

    public BadFilemanDate(String reason, char[] value) {
      super("Invalid date string: " + String.valueOf(value) + ". " + reason);
    }
  }

  private static class DateTimeFormatter {
    private final DecimalFormat numbers = zeroPaddedDecimal();

    public String format(ZonedDateTime zdt) {
      StringBuilder result = new StringBuilder(14);
      result
          .append(zdt.getYear() - 1700)
          .append(zeroPaddedNumber(zdt.getMonthValue()))
          .append(zeroPaddedNumber(zdt.getDayOfMonth()));
      if (!hasValue(zdt.getHour(), zdt.getMinute(), zdt.getSecond())) {
        return result.toString();
      }
      result.append('.');
      if (hasValue(zdt.getMinute(), zdt.getSecond())) {
        result.append(zeroPaddedNumber(zdt.getHour()));
      } else {
        return result.append(noTrailingZero(zdt.getHour())).toString();
      }
      if (hasValue(zdt.getSecond())) {
        result.append(zeroPaddedNumber(zdt.getMinute()));
      } else {
        return result.append(noTrailingZero(zdt.getMinute())).toString();
      }
      return result.append(noTrailingZero(zdt.getSecond())).toString();
    }

    private boolean hasValue(int... values) {
      for (var v : values) {
        if (v != 0) {
          return true;
        }
      }
      return false;
    }

    private String noTrailingZero(int number) {
      if (number % 10 == 0) {
        return Integer.toString(number / 10);
      }
      return zeroPaddedNumber(number);
    }

    private String zeroPaddedNumber(int value) {
      return numbers.format(value);
    }
  }

  private static class Parser {
    final char[] value;

    ZoneId timeZone;

    int index;

    public Parser(String s, ZoneId tz) {
      value = s.toCharArray();
      timeZone = tz;
      index = 0;
    }

    private int next(int chars) {
      require(chars);
      return readInt(chars);
    }

    private int nextIfAvailable(int chars, int defaultValue) {
      if (remaining() == 0) {
        return defaultValue;
      }
      if (remaining() >= chars) {
        return readInt(chars);
      }
      return readRemainingIntAndPadToSize(chars);
    }

    public Instant parse() {
      requireTimeZone();
      final int year = next(3);
      final int month = next(2);
      final int day = next(2);
      requireCharIfAvailable('.');
      int hour = nextIfAvailable(2, 0);
      int minute = nextIfAvailable(2, 0);
      int second = nextIfAvailable(2, 0);
      requireNoMoreChars();
      index = 0;
      return ZonedDateTime.of(year + 1700, month, day, hour, minute, second, 0, timeZone)
          .toInstant();
    }

    private int readInt(int chars) {
      try {
        return Integer.parseInt(String.valueOf(value, index, chars));
      } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
        throw new BadFilemanDate("Contains invalid characters.", value);
      } finally {
        index += chars;
      }
    }

    private int readRemainingIntAndPadToSize(int chars) {
      int notRead = chars - remaining();
      int remainingInt = readInt(remaining());
      if (notRead <= 0) {
        return remainingInt;
      }
      try {
        BigInteger paddingMultiplier = BigInteger.TEN.pow(notRead);
        return paddingMultiplier.multiply(BigInteger.valueOf(remainingInt)).intValueExact();
      } catch (ArithmeticException e) {
        throw new BadFilemanDate("Unable to pad remaining characters.", value);
      }
    }

    private int remaining() {
      return value.length - index;
    }

    void require(int chars) {
      if (remaining() < chars) {
        throw new BadFilemanDate("Not enough characters.", value);
      }
    }

    void requireCharIfAvailable(char c) {
      if (remaining() == 0) {
        return;
      }
      try {
        if (value[index] != c) {
          throw new BadFilemanDate("Missing decimal.", value);
        }
      } finally {
        index++;
      }
    }

    void requireNoMoreChars() {
      if (remaining() > 0) {
        throw new BadFilemanDate("There are extra characters.", value);
      }
    }

    void requireTimeZone() {
      if (timeZone == null) {
        throw new BadFilemanDate("No time zone specified.");
      }
    }
  }
}
