package ro.alexk.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.alexk.backend.entities.Util;

public interface WifiCredentialsRepository extends JpaRepository<Util, Long> {
    @Query("select u.value from Util u where u.name = 'wifi_ssid'")
    String getSSID();
    @Query("select u.value from Util u where u.name = 'wifi_pass'")
    String getPass();

    @Modifying
    @Query("update Util u set u.value = :ssid where u.name = 'wifi_ssid'")
    void setSSID(@Param("ssid") String ssid);

    @Modifying
    @Query("update Util u set u.value = :pass where u.name = 'wifi_pass'")
    void setPass(@Param("pass") String pass);
}
