package ro.alexk.backend.models.websocket;

public record ConfigRequestEvent(String mac, Integer id) {
    public String computeMac() {
        var sb = new StringBuilder(mac);
        if (id != null) {
            sb.append('-').append(id);
        }
        return sb.toString();
    }
}
