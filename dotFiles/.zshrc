spf() {
    os=$(uname -s)

    # macOS
    if [[ "$os" == "Darwin" ]]; then
        export SPF_LAST_DIR="$HOME/Library/Application Support/superfile/lastdir"
    fi

    command spf "$@"

    [ ! -f "$SPF_LAST_DIR" ] || {
        . "$SPF_LAST_DIR"
        rm -f -- "$SPF_LAST_DIR" > /dev/null
    }
}
eval "$(oh-my-posh init zsh --config ~/.clean-detailed.omp.json)"
export EDITOR="hx"

eval $(thefuck --alias tf)

alias reload-zsh="source ~/.zshrc"
alias edit-zsh="hx ~/.zshrc"

# history setup
HISTFILE=$HOME/.zhistory
SAVEHIST=1000
HISTSIZE=999
setopt share_history 
setopt hist_expire_dups_first
setopt hist_ignore_dups
setopt hist_verify

# completion using arrow keys (based on history)
bindkey '^[[A' history-search-backward
bindkey '^[[B' history-search-forward

source $(brew --prefix)/share/zsh-autosuggestions/zsh-autosuggestions.zsh
source $(brew --prefix)/share/zsh-syntax-highlighting/zsh-syntax-highlighting.zsh

export PATH=$PATH:/Users/rafap/.spicetify
export PATH="$HOME/.cargo/bin:$PATH"
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH=$PATH:/Users/rafap/Library/Python/3.9/bin

. "$HOME/.local/bin/env"

export HELIX_RUNTIME=~/2-Coding/rust/helix/runtime/

test -e "${HOME}/.iterm2_shell_integration.zsh" && source "${HOME}/.iterm2_shell_integration.zsh"

export PICO_SDK_PATH=$HOME/pico-sdk
export PATH="/Applications/ArmGNUToolchain/$(ls /Applications/ArmGNUToolchain/)/arm-none-eabi/bin:$PATH"
export PATH="/opt/homebrew/opt/llvm/bin:$PATH"
