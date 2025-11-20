package shop.me.back.end.Service;

import shop.me.back.end.Dto.PurchaseEmailDto;
import shop.me.back.end.Enums.DeliveryStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailSander {

    @Autowired
    private  JavaMailSender javaMailSender;
    public String EmailVerificationOtpSand(String email,String otp){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Account verification otp");
        message.setText("thanks for choosing our plateform please enter below otp to verify and enjoy our online service "+"\n"
                            +"        " +otp );

        javaMailSender.send(message);
        return "Email is sanded to "+email;
    }


    public String EmailForPasswordReset(String email,String otp){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Otp for password reset");
        message.setText("otp  for password reset please dont share it with anyone and if its not you ignore this email "+"\n"
                            +"        " +otp);

        javaMailSender.send(message);
        return "Email is sanded to "+email;
    }

    public String EmailAfterPurchase(String email, PurchaseEmailDto purchaseEmailDto) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Thank you for your purchase, " + purchaseEmailDto.getFullname() + "!");

            String htmlContent = """
                    <div style="font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 20px;">
                        <div style="max-width: 600px; margin: auto; background-color: white; border-radius: 10px; padding: 25px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                            <h2 style="color: #333;">Hi %s,</h2>
                            <p style="font-size: 16px; color: #555;">Thank you for shopping with <strong>ShopMe</strong>! 🎉</p>
                            <p style="font-size: 15px; color: #555;">We’ve received your order and it’s now being processed. Below are your order details:</p>
                            
                            <hr style="border: none; border-top: 2px solid #eee; margin: 20px 0;">
                            
                            <h3 style="color: #222;">🧾 Order Summary</h3>
                            <table style="width: 100%%; border-collapse: collapse;">
                                <tr>
                                    <td style="padding: 8px 0;">Order ID:</td>
                                    <td style="padding: 8px 0; font-weight: bold;">%s</td>
                                </tr>
                                <tr>
                                    <td style="padding: 8px 0;">Order Date:</td>
                                    <td style="padding: 8px 0;">%s</td>
                                </tr>
                            </table>

                            <h3 style="color: #222; margin-top: 20px;">📦 Product Details</h3>
                            <table style="width: 100%%; border: 1px solid #ddd; border-collapse: collapse; margin-top: 10px;">
                                <thead style="background-color: #f4f4f4;">
                                    <tr>
                                        <th style="padding: 10px; border: 1px solid #ddd; text-align: left;">Product Name</th>
                                        <th style="padding: 10px; border: 1px solid #ddd; text-align: left;">Quantity</th>
                                        <th style="padding: 10px; border: 1px solid #ddd; text-align: left;">Price</th>
                                        <th style="padding: 10px; border: 1px solid #ddd; text-align: left;">Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                                        <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                                        <td style="padding: 10px; border: 1px solid #ddd;">₹%s</td>
                                        <td style="padding: 10px; border: 1px solid #ddd;">₹%s</td>
                                    </tr>
                                </tbody>
                            </table>

                            <h3 style="color: #222; margin-top: 20px;">🚚 Shipping Address</h3>
                            <p style="font-size: 15px; color: #555; white-space: pre-line;">%s</p>

                            <p style="font-size: 15px; color: #555; margin-top: 20px;">
                                We’ll notify you as soon as your order is shipped.<br>
                                If you have any questions, reach us at 
                                <a href="mailto:support@shopme.com" style="color: #007bff; text-decoration: none;">support@shopme.com</a>.
                            </p>

                            <p style="font-size: 15px; color: #333;">Thank you once again for choosing <strong>ShopMe</strong> — we truly appreciate your trust in us ❤️</p>

                            <p style="font-size: 15px; color: #555;">Warm regards,<br>
                            <strong>The ShopMe Team</strong></p>
                        </div>
                    </div>
                    """.formatted(
                    purchaseEmailDto.getFullname(),
                    purchaseEmailDto.getOrderNumber(),
                    purchaseEmailDto.getOrderDate(),
                    purchaseEmailDto.getProductName(),
                    purchaseEmailDto.getQuantity(),
                    purchaseEmailDto.getPricePerProduct(),
                    purchaseEmailDto.getTotalAmount(),
                    purchaseEmailDto.getAddress()
            );

            helper.setText(htmlContent, true); // true = HTML enabled
            javaMailSender.send(message);

            return "HTML Purchase Email sent successfully to " + email;

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String EmailAfterCancellation(String email, PurchaseEmailDto purchaseEmailDto) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Your order has been cancelled, " + purchaseEmailDto.getFullname());

            // Prepare nicely formatted strings
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
            String orderDate     = purchaseEmailDto.getOrderDate() != null
                    ? dtf.format(purchaseEmailDto.getOrderDate())
                    : "";
            String cancelDate    = dtf.format(LocalDateTime.now());
            String qtyStr        = String.valueOf(purchaseEmailDto.getQuantity());
            String refundAmount  = String.format("₹%,.2f", purchaseEmailDto.getTotalAmount());  // pre-format currency

            String htmlContent = """
            <div style="font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: white; border-radius: 10px; padding: 25px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                    
                    <h2 style="color: #333;">Hi %s,</h2>
                    <p style="font-size: 16px; color: #555;">Your order has been <strong>successfully cancelled</strong> ❌</p>
                    <p style="font-size: 15px; color: #555;">Below are the details of your cancelled order:</p>

                    <hr style="border: none; border-top: 2px solid #eee; margin: 20px 0;">

                    <h3 style="color: #222;">🧾 Cancellation Summary</h3>
                    <table style="width: 100%%; border-collapse: collapse;">
                        <tr>
                            <td style="padding: 8px 0;">Order ID:</td>
                            <td style="padding: 8px 0; font-weight: bold;">%s</td>
                        </tr>
                        <tr>
                            <td style="padding: 8px 0;">Order Date:</td>
                            <td style="padding: 8px 0;">%s</td>
                        </tr>
                        <tr>
                            <td style="padding: 8px 0;">Cancellation Date:</td>
                            <td style="padding: 8px 0;">%s</td>
                        </tr>
                    </table>

                    <h3 style="color: #222; margin-top: 20px;">📦 Product Details</h3>
                    <table style="width: 100%%; border: 1px solid #ddd; border-collapse: collapse; margin-top: 10px;">
                        <thead style="background-color: #f4f4f4;">
                            <tr>
                                <th style="padding: 10px; border: 1px solid #ddd; text-align: left;">Product Name</th>
                                <th style="padding: 10px; border: 1px solid #ddd; text-align: left;">Quantity</th>
                                <th style="padding: 10px; border: 1px solid #ddd; text-align: left;">Refund Amount</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                                <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                                <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                            </tr>
                        </tbody>
                    </table>

                    <p style="font-size: 15px; color: #555; margin-top: 20px;">
                        Your refund will be processed within <strong>3-5 business days</strong>.<br>
                        If you have any questions, feel free to email us at 
                        <a href="mailto:support@shopme.com" style="color: #007bff; text-decoration: none;">support@shopme.com</a>.
                    </p>

                    <p style="font-size: 15px; color: #333;">We're sorry to see your order cancelled, but we’re always here if you need anything ❤</p>

                    <p style="font-size: 15px; color: #555;">Warm regards,<br>
                    <strong>The ShopMe Team</strong></p>
                </div>
            </div>
            """.formatted(
                    purchaseEmailDto.getFullname(),        // Hi %s,
                    purchaseEmailDto.getOrderNumber(),     // Order ID
                    orderDate,                              // Order Date
                    cancelDate,                             // Cancellation Date
                    purchaseEmailDto.getProductName(),      // Product Name
                    qtyStr,                                 // Quantity
                    refundAmount                            // Refund Amount (already includes ₹ and formatting)
            );

            helper.setText(htmlContent, true);
            javaMailSender.send(message);

            return "HTML Cancellation Email sent successfully to " + email;

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String EmailAfterStatusUpdate(String email, PurchaseEmailDto dto, DeliveryStatus newStatus) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Title depends on status
            String subject = switch (newStatus) {
                case Processing     -> "Your order is now being processed, " + dto.getFullname();
                case Packed         -> "Your order has been packed, " + dto.getFullname();
                case Shipped        -> "Your order is on the way, " + dto.getFullname();
                case OutForDelivery -> "Your order is out for delivery 🚚, " + dto.getFullname();
                case Delivered      -> "Your order has been delivered 🎉, " + dto.getFullname();
                case Returned       -> "Your return request is being processed, " + dto.getFullname();
                default             -> "Update on your order, " + dto.getFullname();
            };

            helper.setTo(email);
            helper.setSubject(subject);

            // Emoji based on status
            String statusEmoji = switch (newStatus) {
                case Processing     -> "🛠️ Processing";
                case Packed         -> "📦 Packed";
                case Shipped        -> "🚚 Shipped";
                case OutForDelivery -> "🛵 Out For Delivery";
                case Delivered      -> "🎉 Delivered";
                case Returned       -> "↩️ Returned";
                default             -> "🧾 Update";
            };

            // Status description
            String statusMessage = switch (newStatus) {
                case Processing     -> "Your order is being prepared by our team.";
                case Packed         -> "Your items have been safely packed and are ready to move.";
                case Shipped        -> "Your package has left our facility and is on its way to you.";
                case OutForDelivery -> "Your order is out for delivery and will reach you soon!";
                case Delivered      -> "Your order has been successfully delivered. We hope you enjoy it!";
                case Returned       -> "Your return request is under review. We’ll update you shortly.";
                default             -> "Your order has been updated.";
            };

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
            String updateDate = dtf.format(LocalDateTime.now());
            String orderDate  = dto.getOrderDate() != null ? dtf.format(dto.getOrderDate()) : "";

            String qtyStr      = String.valueOf(dto.getQuantity());
            String totalAmount = String.format("₹%,.2f", dto.getTotalAmount());

            // HTML template
            String htmlContent = """
        <div style="font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 20px;">
            <div style="max-width: 600px; margin: auto; background-color: white; border-radius: 10px; padding: 25px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">

                <h2 style="color: #333;">Hi %s,</h2>
                <p style="font-size: 16px; color: #555;">%s</p>
                <p style="font-size: 15px; color: #555;">%s</p>

                <hr style="border: none; border-top: 2px solid #eee; margin: 20px 0;">

                <h3 style="color: #222;">📬 Order Status Update</h3>
                <table style="width: 100%%; border-collapse: collapse;">
                    <tr>
                        <td style="padding: 8px 0;">Order ID:</td>
                        <td style="padding: 8px 0; font-weight: bold;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px 0;">Order Date:</td>
                        <td style="padding: 8px 0;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px 0;">Updated On:</td>
                        <td style="padding: 8px 0;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px 0;">Current Status:</td>
                        <td style="padding: 8px 0; font-weight: bold; color: #007bff;">%s</td>
                    </tr>
                </table>

                <h3 style="color: #222; margin-top: 20px;">📦 Product Details</h3>
                <table style="width: 100%%; border: 1px solid #ddd; border-collapse: collapse; margin-top: 10px;">
                    <thead style="background-color: #f4f4f4;">
                        <tr>
                            <th style="padding: 10px; border: 1px solid #ddd; text-align: left;">Product Name</th>
                            <th style="padding: 10px; border: 1px solid #ddd; text-align: left;">Quantity</th>
                            <th style="padding: 10px; border: 1px solid #ddd; text-align: left;">Total Amount</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                            <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                            <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                        </tr>
                    </tbody>
                </table>

                <p style="font-size: 15px; color: #555; margin-top: 20px;">
                    If you need help, feel free to reach us anytime at  
                    <a href="mailto:support@shopme.com" style="color: #007bff; text-decoration: none;">support@shopme.com</a>.
                </p>

                <p style="font-size: 15px; color: #333;">Thank you for shopping with us! 💙</p>

                <p style="font-size: 15px; color: #555;">Warm regards,<br>
                <strong>The ShopMe Team</strong></p>
            </div>
        </div>
        """.formatted(
                    dto.getFullname(),      // Hi %s,
                    statusEmoji,            // Emoji Status
                    statusMessage,          // Meaningful line
                    dto.getOrderNumber(),   // Order ID
                    orderDate,              // Order Date
                    updateDate,             // Updated On
                    newStatus,              // Current Status
                    dto.getProductName(),
                    qtyStr,
                    totalAmount
            );

            helper.setText(htmlContent, true);
            javaMailSender.send(message);

            return "Status update email sent to " + email;

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
