Vagrant.configure(2) do |config|
  config.vm.box = "ubuntu/trusty64"
  config.vm.hostname = "slack-lens"

  config.ssh.forward_agent = true
  config.vm.provider "virtualbox" do |v|
    v.customize ["modifyvm", :id, "--nictype1", "virtio", "--memory", 1024]
  end

  #config.vm.network "forwarded_port", host: 9300, guest: 9300 # ES port
  #config.vm.network "forwarded_port", host: 9200, guest: 9200 # ES port

  dotfiles = %w{  gitconfig gitignore
          profile login logout
          bashrc bash_profile bash_aliases
          zshrc zprofile vimrc scmbrc ssh/id_rsa.pub ssh/id_rsa }

  dotfiles.each do |dotfile|
    dotfile_full = File.expand_path("~/.#{dotfile}")
    if File.exists?(dotfile_full)
      config.vm.provision "file", source: dotfile_full, destination: "/home/vagrant/.#{dotfile}"
    end
  end

  syncdirs = %w{ .aws .vim .oh-my-zsh .scm-breeze bin }
  syncdirs.each do |syncdir|
    syncdir_full = File.expand_path("~/#{syncdir}")
    if File.directory?(syncdir_full)
      config.vm.synced_folder syncdir_full, "/home/vagrant/#{syncdir}"
    end
  end

  config.vm.synced_folder "../slack-lens", "/home/vagrant/dev/slack-lens"
  config.vm.provision :shell, privileged: false, path: "scripts/vagrant-bootstrap.sh"
end
