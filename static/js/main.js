// JBAC – Main JavaScript

// Auto-dismiss alerts after 5 seconds
document.addEventListener('DOMContentLoaded', function () {
  setTimeout(function () {
    document.querySelectorAll('.alert.alert-dismissible').forEach(function (el) {
      const bsAlert = bootstrap.Alert.getOrCreateInstance(el);
      bsAlert.close();
    });
  }, 5000);
});
